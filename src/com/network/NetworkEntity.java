package com.network;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.gui.Table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;

public abstract class NetworkEntity extends Thread{
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;
    protected Socket connectionHandle;
    protected Object receivedMessage;


    NetworkEntity(final String name){
        super(name);
    }

    public abstract void run();


    public void getStreams() throws IOException{
        outputStream= new ObjectOutputStream(connectionHandle.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(connectionHandle.getInputStream());

    }

    public void closeConnection(){
        try{
            if (outputStream != null){
                outputStream.close();
            }
            if (inputStream != null){
                inputStream.close();
            }

            if(connectionHandle != null){
                connectionHandle.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void processIncomingData() throws IOException{
        do{
            try{
                receivedMessage = inputStream.readObject();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (receivedMessage instanceof Move){
                final Move m = (Move) receivedMessage;
                Table.get().updateComputerMove(m);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(m).getBoard());
                Table.get().getMoveLog().addMove(m);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(),Table.get().getMoveLog());
                Table.get().getTakenPiecePanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());

            } else if (receivedMessage instanceof Board){
                final Board b = (Board) receivedMessage;
            } else if (receivedMessage instanceof String){

            }
        } while (receivedMessage != null);
    }
    public void sendData(final Object objToSend){
        try {
            outputStream.writeObject(objToSend);
            outputStream.flush();
            System.out.println("Send Move successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
