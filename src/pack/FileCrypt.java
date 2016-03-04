package pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import pack.Crypto;

public class FileCrypt {
	static boolean suc=true;
	static Thread Enc,Dec;
	private static java.lang.Process proc;
	public static boolean encrypt(String path) {	
		final File file=new File(path);
			if(file.isDirectory()){
			for(final File child : file.listFiles()){
				if(child.isDirectory()){
					encrypt(child.getAbsolutePath());
				}
				if(!child.isDirectory()&&child.getName().lastIndexOf(".enc")==-1){
			Runnable runner=new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Path p=child.toPath();
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(child);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					File fie=new File(child.getAbsolutePath()+".enc");
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(fie);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					byte[] k=Crypto.decrypt("Q]=-737506").getBytes();
					SecretKeySpec key=new SecretKeySpec(k,"DES");
					Cipher encrypt = null;
					try {
						encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
					} catch (NoSuchAlgorithmException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					} catch (NoSuchPaddingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					try {
						encrypt.init(Cipher.ENCRYPT_MODE,key);
					} catch (InvalidKeyException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					CipherOutputStream cos=new CipherOutputStream(fos,encrypt);
					byte[] buf=new byte[1024];
					int read=0;
					try {
						while((read=fis.read(buf))!=-1)
							cos.write(buf,0,read);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						fis.close();
						cos.flush();
						cos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try{
						if(p.toFile().exists()){
							p.toFile().delete();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
			};
				Enc=new Thread(runner);
				Enc.start();
				
				}
				}
			}
			try {
				Enc.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException e){}
			try {
				proc=Runtime.getRuntime().exec("attrib +h +s +r "+"\""+file.getAbsolutePath()+"\"");
				proc.waitFor();
				proc.destroy();
				System.out.println("Folder Hidden!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				suc=false;
			}
			return suc;
		}
		public static boolean decrypt(String path) {
			final File file=new File(path);
			try {
				proc=Runtime.getRuntime().exec("attrib -h -s -r "+"\""+file.getAbsolutePath()+"\"");
				proc.waitFor();
				proc.destroy();
				System.out.println("Folder UnHidden!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				suc=false;
			}
			if(file.isDirectory()){
			for(final File child : file.listFiles()){
				if(child.isDirectory()){
					decrypt(child.getAbsolutePath());
				}
				if(!child.isDirectory()&&child.getName().lastIndexOf(".enc")!=-1){
				Runnable runner=new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Path p=child.toPath();
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(child);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					File fie=new File(child.getAbsolutePath().split(".enc")[0]);
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(fie);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					byte[] k=Crypto.decrypt("Q]=-737506").getBytes();
					SecretKeySpec key=new SecretKeySpec(k,"DES");
					Cipher decrypt = null;
					try {
						decrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					} catch (NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						decrypt.init(Cipher.DECRYPT_MODE,key);
					} catch (InvalidKeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					CipherInputStream cis=new CipherInputStream(fis,decrypt);
					byte[] buf=new byte[1024];
					int read=0;
					try {
						while((read=cis.read(buf))!=-1)
							fos.write(buf,0,read);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						cis.close();
						fos.flush();
						fos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try{
						if(p.toFile().exists()){
							p.toFile().delete();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
			};
			Dec= new Thread(runner);
			Dec.start();
				}
			}
			}
			try {
				Dec.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException e){}
			return suc;
		}
		public static boolean encryptFile(String path){
			final File child=new File(path);
			Runnable runner=new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Path p=child.toPath();
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(child);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					File fie=new File(child.getAbsolutePath()+".enc");
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(fie);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					byte[] k=Crypto.decrypt("Q]=-737506").getBytes();
					SecretKeySpec key=new SecretKeySpec(k,"DES");
					Cipher encrypt = null;
					try {
						encrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
					} catch (NoSuchAlgorithmException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					} catch (NoSuchPaddingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					try {
						encrypt.init(Cipher.ENCRYPT_MODE,key);
					} catch (InvalidKeyException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						suc=false;
					}
					CipherOutputStream cos=new CipherOutputStream(fos,encrypt);
					byte[] buf=new byte[1024];
					int read=0;
					try {
						while((read=fis.read(buf))!=-1)
							cos.write(buf,0,read);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						fis.close();
						cos.flush();
						cos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try{
						if(p.toFile().exists()){
							p.toFile().delete();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
			};
			Enc=new Thread(runner);
			Enc.start();
			try {
				Enc.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException e){}
			try {
				proc=Runtime.getRuntime().exec("attrib +h +s +r "+"\""+child.getAbsolutePath()+".enc\"");
				proc.waitFor();
				proc.destroy();
				System.out.println("File Hidden!!");
				suc=true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				suc=false;
			}
			return suc;
		}
		public static boolean decryptFile(String path){
			final File child=new File(path);
			try {
				proc=Runtime.getRuntime().exec("attrib -h -s -r "+"\""+child.getAbsolutePath()+"\"");
				proc.waitFor();
				proc.destroy();
				System.out.println("File UnHidden!!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				suc=false;
			}
			if(!child.isDirectory()&&child.getName().lastIndexOf(".enc")!=-1){
				Runnable runner=new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Path p=child.toPath();
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(child);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					File fie=new File(child.getAbsolutePath().split(".enc")[0]);
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(fie);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					byte[] k=Crypto.decrypt("Q]=-737506").getBytes();
					SecretKeySpec key=new SecretKeySpec(k,"DES");
					Cipher decrypt = null;
					try {
						decrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					} catch (NoSuchPaddingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						decrypt.init(Cipher.DECRYPT_MODE,key);
					} catch (InvalidKeyException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					CipherInputStream cis=new CipherInputStream(fis,decrypt);
					byte[] buf=new byte[1024];
					int read=0;
					try {
						while((read=cis.read(buf))!=-1)
							fos.write(buf,0,read);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try {
						cis.close();
						fos.flush();
						fos.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						suc=false;
					}
					try{
						if(p.toFile().exists()){
							p.toFile().delete();
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
			};
			Dec= new Thread(runner);
			Dec.start();
			try {
				Dec.join();
				suc=true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				suc=false;
			}
			}
			return suc;
		}
		public static void main(String args[]) {
			String mode=args[1];
			if(mode.equals("-e")){
				if(encrypt(args[0]))
				System.out.println("Done");
			}
			else if(mode.equals("-d")){	
				if(decrypt(args[0]))
				System.out.println("Done");
			}
			else if(mode.equals("-ef")){
				encryptFile(args[0]);
				System.out.println("Done");
			}
			else if(mode.equals("-df")){	
				decryptFile(args[0]);
				System.out.println("Done");
			}
			else
				System.out.println("Invalid Mode!!\n-e : Encrypt Folder\n-d : Decrypt Folder\n-ef : Encrypt File\n-df : Decrypt File");
		}
	}
