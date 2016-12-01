package com.excelsiorsoft.multihardware_reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {

	protected int serverPort = 8089;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;

	/*public HttpServer(int port) {
		this.serverPort = port;
	}*/

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}

		openServerSocket();
		System.out.println("HWServer starting, [" + this.serverPort + "]");
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				System.out.println("before accepting");
				clientSocket = this.serverSocket.accept();
				
			} catch (IOException e) {
				e.printStackTrace();
				if (isStopped()) {
					System.out.println("HWServer stoped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection",
						e);
			}
			try {
				processClientRequest(clientSocket);
				System.out.println("after accepting");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void processClientRequest(Socket clientSocket) throws IOException {

		InputStream input = clientSocket.getInputStream();
		OutputStream output = clientSocket.getOutputStream();
		new KBSendInput("Talked to HW1");
		//long time = System.currentTimeMillis();
		output.write(("HTTP/1.1 200 OK\n\n<html><body><input autofocus=\"autofocus\" size=10></body></html>"
				/*"<html><body><h1>Hello World</h1><img width=1 height=1 src=\"http://localhost:8089\"><input autofocus=\"autofocus\" size=10></body></html>"*/)
				.getBytes());
		output.close();
		input.close();
		System.out.println(".");
		


	}

	private boolean isStopped() {
		return this.isStopped;
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port" + this.serverPort);
		}

	}
	
	public synchronized void stop(){
		this.isStopped = true;
		try{
			this.serverSocket.close();
		}catch(IOException e){
			throw new RuntimeException("Error closing server", e);
		}
	}
	
	
	public static void main(String ... args){
		new HttpServer(/*8089*/).run();
	}

}
