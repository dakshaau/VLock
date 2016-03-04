package pack;

import pack.FFT;
import java.sql.*;
import pack.Complex;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import javax.sound.sampled.*;

public class Authentify {
	
	static AudioFormat af=new AudioFormat(44100,8,2,true,true);
	ByteArrayOutputStream out;
	static DataLine.Info info=new DataLine.Info(TargetDataLine.class, af);
	static TargetDataLine line;
	static int buffer=1024*af.getFrameSize();
	static int bytes=af.getFrameSize();
	static int hitnum=16;
	static int hit1[]=new int[hitnum];
	byte[] audio;
	boolean stopped=false;
	Thread Set,rcrd;
	Connection c;
	Statement stmt;
	private int id;
	
	Authentify(){
		for(int i=0;i<hitnum;i++)
			hit1[i]=0;
	}
	
	/*
	 * record() method records the audio and stores the audio bytes in the 'audio' Byte Array
	 */
	boolean record(){
		out=new ByteArrayOutputStream();
		try {
			info=new DataLine.Info(TargetDataLine.class, af);
			line=(TargetDataLine)AudioSystem.getLine(info);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * The following thread generates a 3 second ideal for getting a voice sample
		 */
		Runnable runner = new Runnable(){
			@Override
			public void run() {
				try {
					for(int i=0;i<101;i++)
						Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					System.out.println("Enough!");
					stopped=true;
				}
			}
		};
		Thread Stop=new Thread(runner);
		Stop.start();
		try {
			line.open(af);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * This following thread does the recording from the capture device
		 */
		runner=new Runnable(){

			@Override
			public void run() {
				byte[] read=new byte[line.getBufferSize()];
				int bytesread=0;
				System.out.println("Start Talking!");
				line.start();
				while(!stopped){
					bytesread=line.read(read, 0, read.length);
					out.write(read,0,bytesread);
				}
				line.drain();
				line.close();
				line=null;
				audio=out.toByteArray();
			}
			
		};
		rcrd=new Thread(runner);
		rcrd.setPriority(Thread.MAX_PRIORITY);
		rcrd.start();
		try {
			rcrd.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	/*
	 * For finding the frequency hits in the recorded data
	 */
	void setHits()
	{
		try {
			if(rcrd.isAlive())
				rcrd.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Runnable runner=new Runnable(){
			Complex[] cmplx=new Complex[buffer];
			InputStream input = new ByteArrayInputStream(audio);
			AudioInputStream a=new AudioInputStream(input,af,audio.length/af.getFrameSize());
			int amplification=100;
			double[] doubledata=new double[buffer];
			@Override
			public void run() {
				int bytesread=0;
				byte[] l=new byte[buffer];
				try {
					/*
					 * This loop converts byte data into float data and creates an input array for the Complex class
					 * so that FFT (to calculate frequency) can be applied over it
					 */
					while((bytesread=a.read(l))!=-1){
						for (int index = 0, floatIndex = 0; index < bytesread - bytes + 1; index += bytes, floatIndex++) {
							double sample = 0;
							for (int b = 0; b < bytes; b++) {
								int v = l[ index + b];
								if (b < bytes - 1 || bytes == 1) {
									v &= 0xFF;
								}
								sample += v << (b * 8);
							}
							double sample32 = amplification * (sample / 32768.0);
							doubledata[floatIndex] = sample32;
						}
						for(int i=0;i<doubledata.length;i++)
							cmplx[i]=new Complex(doubledata[i],0);
						Complex[] fft=FFT.fft(cmplx);
						for(int i=0;i<fft.length;i++)
						{
							if(fft[i].abs()>=300&&fft[i].abs()<500)
								hit1[0]++;
							if(fft[i].abs()>=500&&fft[i].abs()<1000)
								hit1[1]++;
							if(fft[i].abs()>=1000&&fft[i].abs()<2000)
								hit1[2]++;
							if(fft[i].abs()>=2000&&fft[i].abs()<3000)
								hit1[3]++;
							if(fft[i].abs()>=3000&&fft[i].abs()<4000)
								hit1[4]++;
							if(fft[i].abs()>=4000&&fft[i].abs()<5000)
								hit1[5]++;
							if(fft[i].abs()>=5000&&fft[i].abs()<6000)
								hit1[6]++;
							if(fft[i].abs()>=6000&&fft[i].abs()<7000)
								hit1[7]++;
							if(fft[i].abs()>=7000&&fft[i].abs()<8000)
								hit1[8]++;
							if(fft[i].abs()>=8000&&fft[i].abs()<9001)
								hit1[9]++;
							if(fft[i].abs()>=150&&fft[i].abs()<300)
								hit1[10]++;
							if(fft[i].abs()>9000&&fft[i].abs()<12000)
								hit1[11]++;
							if(fft[i].abs()>=12000&&fft[i].abs()<14000)
								hit1[12]++;
							if(fft[i].abs()>=14000&&fft[i].abs()<16000)
								hit1[13]++;
							if(fft[i].abs()>=16000&&fft[i].abs()<18000)
								hit1[14]++;
							if(fft[i].abs()>=18000&&fft[i].abs()<=20000)
								hit1[15]++;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Set=new Thread(runner);
		if(!audio.equals(null))
			Set.start();
		else
		{
			record();
			setHits();
		}	
	}
	
	/*
	 * Overloaded setHits function to initialize hit1 from the data from a .wav file
	 */
	void setHits(final String Path)
	{
		Runnable runner=new Runnable(){
			Complex[] cmplx=new Complex[buffer];
			final File in=new File(Path);
			
			int amplification=100;
			double[] doubledata=new double[buffer];
			@Override
			public void run() {
				AudioFormat af=new AudioFormat(44100,8,2,true,true);
				AudioInputStream A = null;
				try {
					A = AudioSystem.getAudioInputStream(in);
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				AudioInputStream temp=null;
				if(!af.equals(A.getFormat()))
				{
					if(AudioSystem.isConversionSupported(af,A.getFormat())){
						temp=AudioSystem.getAudioInputStream(af,A);
						A=temp;
						temp=null;
					}
				}
				System.out.println("\nFile "+in.getName()+" read!!\n");
				final AudioInputStream a=A;
				int bytesread=0;
				byte[] l=new byte[buffer];
				try {
					/*
					 * This loop converts byte data into float data and creates an input array for the Complex class
					 * so that FFT (to calculate frequency) can be applied over it
					 */
					while((bytesread=a.read(l))!=-1){
						for (int index = 0, floatIndex = 0; index < bytesread - bytes + 1; index += bytes, floatIndex++) {
							double sample = 0;
							for (int b = 0; b < bytes; b++) {
								int v = l[ index + b];
								if (b < bytes - 1 || bytes == 1) {
									v &= 0xFF;
								}
								sample += v << (b * 8);
							}
							double sample32 = amplification * (sample / 32768.0);
							doubledata[floatIndex] = sample32;
						}
						for(int i=0;i<doubledata.length;i++)
							cmplx[i]=new Complex(doubledata[i],0);
						Complex[] fft=FFT.fft(cmplx);
						for(int i=0;i<fft.length;i++)
						{
							if(fft[i].abs()>=300&&fft[i].abs()<500)
								hit1[0]++;
							if(fft[i].abs()>=500&&fft[i].abs()<1000)
								hit1[1]++;
							if(fft[i].abs()>=1000&&fft[i].abs()<2000)
								hit1[2]++;
							if(fft[i].abs()>=2000&&fft[i].abs()<3000)
								hit1[3]++;
							if(fft[i].abs()>=3000&&fft[i].abs()<4000)
								hit1[4]++;
							if(fft[i].abs()>=4000&&fft[i].abs()<5000)
								hit1[5]++;
							if(fft[i].abs()>=5000&&fft[i].abs()<6000)
								hit1[6]++;
							if(fft[i].abs()>=6000&&fft[i].abs()<7000)
								hit1[7]++;
							if(fft[i].abs()>=7000&&fft[i].abs()<8000)
								hit1[8]++;
							if(fft[i].abs()>=8000&&fft[i].abs()<9001)
								hit1[9]++;
							if(fft[i].abs()>=150&&fft[i].abs()<300)
								hit1[10]++;
							if(fft[i].abs()>9000&&fft[i].abs()<12000)
								hit1[11]++;
							if(fft[i].abs()>=12000&&fft[i].abs()<14000)
								hit1[12]++;
							if(fft[i].abs()>=14000&&fft[i].abs()<16000)
								hit1[13]++;
							if(fft[i].abs()>=16000&&fft[i].abs()<18000)
								hit1[14]++;
							if(fft[i].abs()>=18000&&fft[i].abs()<=20000)
								hit1[15]++;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		Set=new Thread(runner);
		Set.setPriority(Thread.MAX_PRIORITY);
		Set.start();
	}
	
	/*
	 * This Function creates a Binary File with the data from voice sample just recorded
	 */
	String createSample(String name){
		try{
			Class.forName("org.sqlite.JDBC");
			c=DriverManager.getConnection("jdbc:sqlite:VLD.db");
			stmt=c.createStatement();
			String sql="select USERID from USER where UNAME='"+name+"';";
			ResultSet rs=stmt.executeQuery(sql);
			id=rs.getInt(1);
			stmt.close();
			c.close();
		} catch(Exception e){}
		try {
			if(Set.isAlive())
				Set.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File out=null;
		int _Name=0;
		String  extension="";
		File dir = new File("./Samples");
		if(!dir.exists()){
			try {
				Files.createDirectory(dir.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("dir : "+dir.getPath());
		if(dir.isDirectory()){
			for (File child : dir.listFiles()) {
				int i = child.getName().lastIndexOf('.');
				if (i > 0) {
					extension = child.getName().substring(i);
				}
				if(extension.equals(".VS")){
					_Name=Integer.parseInt(child.getName().substring(0, i));
				}
			}
		}
		String FileName=Integer.toString(_Name+1)+".VS";
		out=new File(dir.getAbsolutePath()+"/"+FileName);
		byte[] output=new byte[hitnum*4];
		byte[] nam=name.getBytes();
		for(int i=0,j=0;i<hitnum;i++){
			byte[] nEw= ByteBuffer.allocate(4).putInt(hit1[i]).array();
			for(int k=0;k<4;k++)
				output[j++]=nEw[k];
		}
		BufferedOutputStream bd = null;
		try {
			bd = new BufferedOutputStream(new FileOutputStream(out));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bd.write(output);
			bd.write(nam);
			bd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("File Written!! "+out.getName());
		return out.getAbsolutePath();
	}
	
	
	/*
	 * Creates a .wav file from the recorded data
	 */
	void createWAV(String name){
		try {
			if(rcrd.isAlive())
				rcrd.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File out=null;
		int _Name=0;
		String  extension="";
		File dir = new File("./Audio");
		if(!dir.exists()){
			try {
				Files.createDirectory(dir.toPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(dir.isDirectory()){
			for (File child : dir.listFiles()) {
				int j = child.getName().indexOf(name);
				int i = child.getName().lastIndexOf('.');
				if (i > 0) {
					extension = child.getName().substring(i);
				}
				if(extension.equals(".wav")&&j>=0){
					_Name=Integer.parseInt(child.getName().substring(name.length(), i));
				}
			}
		}
		String FileName=name+Integer.toString(_Name+1)+".wav";
		out=new File(dir.getAbsolutePath()+"/"+FileName);
		boolean conv=false;
		InputStream input = new ByteArrayInputStream(audio);
		final AudioFormat Format = new AudioFormat(44100, 32, 2, true,true);
		final AudioFormat format1 = new AudioFormat(44100, 8, 2, true,true);
		final AudioInputStream ais = new AudioInputStream(input, format1, audio.length/format1.getFrameSize());
	    AudioInputStream Ais=null;
	    if(AudioSystem.isConversionSupported(Format, ais.getFormat()))
	    {
	    	Ais=AudioSystem.getAudioInputStream(Format,ais);
	    	conv=true;
	    	System.out.println('\n'+"Conversion Possible!!");
	    }
	    if(!conv){
			try {
				AudioSystem.write(ais,AudioFileFormat.Type.WAVE, out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		else
		{
			try {
				AudioSystem.write(Ais, AudioFileFormat.Type.WAVE, out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Play the recorded sound
	 */
	SourceDataLine line1;
	boolean play()
	{
		if(rcrd.isAlive())
			try {
				rcrd.join();
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		byte[] aud=audio;
		InputStream input = new ByteArrayInputStream(aud);
		info=new DataLine.Info(SourceDataLine.class,af);
		try {
			line1 = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	    final AudioInputStream ais = new AudioInputStream(input, af, audio.length/af.getFrameSize());
	    Runnable runner=new Runnable(){
	    	@Override
			public void run() {
					// TODO Auto-generated method stub
					try {
						line1.open(af);
					} catch (LineUnavailableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		line1.start();
		    		int bufferSize = (int) af.getSampleRate() * af.getFrameSize()*3;
		    		byte buffer1[] = new byte[bufferSize];
		    		try {
		    		int count;
	        		while ((count = ais.read(buffer1, 0, buffer1.length)) != -1) {
	        			if (count > 0) {
	        				line1.write(buffer1, 0, count);
		        		}
		    		}
		    		line1.drain();
		    		line1.close();
		    		line1=null;
		    		} catch (IOException e) {
		    		System.err.println("I/O problems: " + e);
		       		System.exit(-3);
		    	}
			} 
	    };
	    Thread ply=new Thread(runner);
	    ply.setPriority(Thread.MAX_PRIORITY);
	    ply.start();
	    try {
			ply.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
	}
	
	/*
	 * Compare the recorded and offline data to find the identity
	 */
	String name="";
	String compare(){
		if(Set.isAlive())
			try {
				Set.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		final int[] hit2=new int[hitnum];
		Runnable runner = new Runnable(){

			@Override
			public void run() {
				String  _Name="";
				File dir = new File("./Samples");
				if(dir.isDirectory()){
					System.out.println("No. of files : "+dir.listFiles().length);
					for (File child : dir.listFiles()) {
						if(child.getName().endsWith(".VS")){
							byte[] output=new byte[hitnum*4];
							byte[] nam = new byte[hitnum*4];
							BufferedInputStream inh=null;
							try {
								inh = new BufferedInputStream(new FileInputStream(child));
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								int i=inh.read(output);
								inh.read(nam,0,(int)child.length()-i);
								_Name=new String(nam);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NullPointerException e){}
							System.out.println("File Read!!");
							for(int k=0,j=0;k<hitnum;k++,j+=4){
								/*byte[] nEw=new byte[4];
								for(int k=0;k<4;k++)
									nEw[k]=output[j++];*/
								hit2[k]=ByteBuffer.wrap(output,j,4).getInt();
							}
							try {
								inh.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							int c=0;
							float sum=0.0f,per;
							for(int k=0;k<hitnum;k++)
							{
								if(hit2[k]!=0){
									sum+=((1.0f*hit1[k])/(1.0f*hit2[k]));
									c++;
								}
							}
							per=(sum/c)*100.0f;
							//System.out.println("Result : "+per);
							if(per>=70&&per<=130){
								name=_Name;
								break;
							}
							else
								name="Error";
						}
					}
				}
			}
		};
		Thread cmpr=new Thread(runner);
		cmpr.setPriority(Thread.MAX_PRIORITY);
		cmpr.start();
		try {
			cmpr.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

/*
 * Read a previously created sample
 */
	String readSample(String path){
		String ret="";
		File p=new File(path);
		int[] hit2=new int[hitnum];
		byte[] output=new byte[hitnum*4];
		byte[] nam = new byte[hitnum*4];
		for(int i=0;i<hitnum;i++)
			hit2[i]=0;
		BufferedInputStream inh=null;
		try {
			inh = new BufferedInputStream(new FileInputStream(p));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int i=inh.read(output);
			inh.read(nam,0,(int)p.length()-i);
			name=new String(nam);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){ System.err.println("NULL");}
		System.out.println("File Read!!");
		for(int k=0,j=0;k<hitnum;k++,j+=4){
			/*byte[] nEw=new byte[4];
			for(int k=0;k<4;k++)
				nEw[k]=output[j++];*/
			hit2[k]=ByteBuffer.wrap(output,j,4).getInt();
		}
		try {
			inh.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret=ret.concat("Name : "+name+"\n\n");
		for(int i=0;i<hitnum;i++)
		{
			switch(i)
			{
			case 0:
				ret=ret.concat("300-499Hz : "+hit2[i]+"\n");
				break;
			
			case 1:
				ret=ret.concat("500-999Hz : "+hit2[i]+"\n");
				break;
				
			case 2:
				ret=ret.concat("1000-1999Hz : "+hit2[i]+"\n");
				break;
				
			case 3:
				ret=ret.concat("2000-2999Hz : "+hit2[i]+"\n");
				break;
				
			case 4:
				ret=ret.concat("3000-3999Hz : "+hit2[i]+"\n");
				break;
				
			case 5:
				ret=ret.concat("4000-4999Hz : "+hit2[i]+"\n");
				break;
				
			case 6:
				ret=ret.concat("5000-5999Hz : "+hit2[i]+"\n");
				break;
				
			case 7:
				ret=ret.concat("6000-6999Hz : "+hit2[i]+"\n");
				break;
				
			case 8:
				ret=ret.concat("7000-7999Hz : "+hit2[i]+"\n");
				break;
				
			case 9:
				ret=ret.concat("8000-9000Hz : "+hit2[i]+"\n");
				break;
				
			case 10:
				ret=ret.concat("150-299Hz : "+hit2[i]+"\n");
				break;
				
			case 11:
				ret=ret.concat("9000-11999Hz : "+hit2[i]+"\n");
				break;
				
			case 12:
				ret=ret.concat("12000-13999Hz : "+hit2[i]+"\n");
				break;
				
			case 13:
				ret=ret.concat("14000-15999Hz : "+hit2[i]+"\n");
				break;
				
			case 14:
				ret=ret.concat("16000-17999Hz : "+hit2[i]+"\n");
				break;
				
			case 15:
				ret=ret.concat("18000-20000Hz : "+hit2[i]+"\n");
				break;
			}
		}
		/*if(ret.equals(""))
			System.out.println("NULL");
		else
			System.out.println(ret);*/
		return ret;
	}
}
