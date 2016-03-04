package pack;

public class Crypto {
	public static String encrypt(String d)
	{
		String nameenc="";
		char ch;
		int key=(int)(Math.random()*5)+5;
		int ke=key;
		for(int i=0;i<d.length();i++)
		{
			ch=d.charAt(i);
			if(i%2==0){
				ch+=ke--;
				nameenc+=ch;
				System.out.print((int)ch+" ");
			}
			else if(i%2!=0){
				ch-=ke--;
				nameenc+=ch;
				System.out.print((int)ch+" ");
			}
		}
		if(key%10==key){
			nameenc+='0';
			nameenc+=key;
		}
		else
			nameenc+=key;
		return nameenc;
	}
	public static String decrypt(String e)
	{
		String key="",name="";
		int i=e.length()-2;
		key+=e.charAt(i++);
		key+=e.charAt(i);
		i=Integer.parseInt(key);
		char ch;
		for(int j=0;j<e.length()-2;j++)
		{
			ch=e.charAt(j);
			if(j%2==0){
				ch-=i--;
				name+=ch;
			}
			else if(j%2!=0){
				ch+=i--;
				name+=ch;
			}
		}
		return name;
	}
	public static void main(String args[]){
		System.out.println(encrypt("noreply.vlock@gmail.com"));
	}
}
