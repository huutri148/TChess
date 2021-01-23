package com.chess.engine.gui;

import com.network.Client;
import com.network.NetworkEntity;
import com.network.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MultiPlayerSetup extends JDialog {
    private final JTextField hostName;
    private final JTextField portNumber;
    public boolean isConnected ;


    public NetworkEntity networkEntity;

    MultiPlayerSetup(final JFrame frame,
                     final boolean modal){
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0,1));
        this.hostName  = new JTextField("localhost");
        this.portNumber = new JTextField("14");
        isConnected= false;
        getContentPane().add(myPanel);
        myPanel.add(new JLabel("Host"));
        myPanel.add(hostName);
        myPanel.add(new JLabel("Port"));
        myPanel.add(portNumber);

        final JButton cancelButton = new JButton("Cancel");
        final JButton createButton = new JButton("Create");
        final JButton connectButton = new JButton("Connect");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int _portNumber = Integer.parseInt(portNumber.getText());
                System.out.println(_portNumber);
                networkEntity = new Server(_portNumber);
                MultiPlayerSetup.this.setVisible(false);
                networkEntity.start();
                networkEntity.run();
                isConnected = true;

            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String host = hostName.getText();
                System.out.println(host);
                int port = Integer.parseInt(portNumber.getText());
                System.out.println(port);
                connect(host,port);
                MultiPlayerSetup.this.setVisible(false);
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                MultiPlayerSetup.this.setVisible(false);
            }
        });


        myPanel.add(cancelButton);
        myPanel.add(createButton);
        myPanel.add(connectButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser(){
        setVisible(true);
        repaint();
    }
    public NetworkEntity getNetworkEntity() {
        return networkEntity;
    }
    private void connect(final String host,final int port){
        networkEntity = new Client(host, port);
        networkEntity.start();
        isConnected = true;
        System.out.println("Successfully connected to the server.");
    }
}
