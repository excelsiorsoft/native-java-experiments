package com.excelsiorsoft.multihardware_reader;

import com.sun.jna.LastErrorException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.KEYBDINPUT;
import com.sun.jna.win32.W32APIOptions;

public class KBSendInput {
	
	public static final MyUser32 user32 = MyUser32.INSTANCE;
	
	//boolean quit = false;
	public String str;
	
	public KBSendInput(String str){
		WinUser.INPUT[] in = sendKBInput(str);
		WorkerThread w = new WorkerThread(in);
		w.start();
	}

	private WinUser.INPUT[]  sendKBInput(String str) {

		this.str = str;
		int size = 2 * str.length();
		final WinUser.INPUT[] in = (WinUser.INPUT[]) new WinUser.INPUT().toArray(size);
		
		for(int i = 0; i < size; i++){
			in[i].type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
			in[i].input = new WinUser.INPUT.INPUT_UNION();
			in[i].input.setType(KEYBDINPUT.class);
		}
		
		int pos = 0;
		for (int i = 0; i < str.length(); i++){
			int code = user32.VkKeyScan(str.charAt(i));
			in[pos].input.ki.wVk = new WinDef.DWORD(code).getLow();
			in[pos].input.ki.dwFlags = new WinDef.DWORD(0);
			in[pos + 1].input.ki.wVk = new WinDef.DWORD(code).getLow();
			in[pos + 1].input.ki.dwFlags = new WinDef.DWORD(WinUser.KEYBDINPUT.KEYEVENTF_KEYUP);
			pos += 2;
		}
		
		return in;
		//quit = true;
		
		
	}
	
	public interface MyUser32 extends Library{
		
		MyUser32 INSTANCE = (MyUser32)Native.loadLibrary("user32",MyUser32.class, W32APIOptions.DEFAULT_OPTIONS);
		int SendInput(int nInputs, WinUser.INPUT[] pInputs, int cbSize);
		int GetMessage(WinUser.MSG lpMsg, WinDef.HWND hwnd, int wMsgFilterMin, int wMsgFilterMax);
		boolean TranslateMessage(WinUser.MSG lpMsg);
		WinDef.LRESULT DispatchMessage(WinUser.MSG lpMsg);
		int VkKeyScan(char a);
	}
	
	
	
	public static void main (String... args){
		
		new KBSendInput("This is real cool");
		
		
	}
	
public static class WorkerThread extends Thread {
		
		WinUser.INPUT[] in;
		public WorkerThread(WinUser.INPUT[] in){
			this.in = in;
			System.out.println("in.size: " + in.length);
			setDaemon(false);
		}
		
		public void run(){
			System.out.println("Ready to send...");
			/*try{
				Thread.sleep(3000);
			}catch(Exception e){}
			*/
			int out = user32.SendInput(in.length, in, in[0].size());
			if(out == 0){
				throw new LastErrorException(Native.getLastError());
			}
		
		}
	}
}
