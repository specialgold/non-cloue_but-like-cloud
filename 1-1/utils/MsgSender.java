package com.dsu.noncloud.utils;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class MsgSender extends Thread {

    Socket socket;
    String msg;
    DataOutputStream dos;
    FileInputStream fis;
    BufferedInputStream bis;

    public MsgSender(Socket socket, String msg) {

        this.socket = socket;
        this.msg = msg;

        try {
            // ������ ���ۿ� ��Ʈ�� ����
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //@Override
    public void run() {

        try {
            //�������� ������ ����('msg' ����)
            dos.writeUTF("msg");
            dos.flush();

            dos.writeUTF( msg );
            dos.flush();

            System.out.println("[" + msg + "] ����");

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try { dos.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }
}