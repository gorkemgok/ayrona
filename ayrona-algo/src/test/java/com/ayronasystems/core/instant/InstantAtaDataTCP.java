package com.ayronasystems.core.instant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by gorkemgok on 14/06/16.
 */
public class InstantAtaDataTCP {

    public static void main(String[] args) throws Exception{
        String sentence;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (System.in));
        Socket clientSocket = new Socket ("172.16.192.40", 7000);
        while ((sentence = bufferedReader.readLine()) != null){
            System.out.println (sentence);
        }

        clientSocket.close();
    }

}
