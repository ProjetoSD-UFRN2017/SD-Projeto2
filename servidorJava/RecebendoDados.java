import java.io.*;  
import java.net.*; 

public class RecebendoDados {
	
	private ServerSocket server;
	
	private String fromClientePWM;
	private String fromClienteEixoX;
	private String status;
	
	private int curvaSelecionada;

	public RecebendoDados() throws IOException {
		server = new ServerSocket(8015);
		status = "Servidor inciado, aguardando conexão...";
	}
	
	public void startRetrievingData() throws IOException{
		
		Socket client = server.accept();
		status = "Servidor conectado";
		
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		
		out.println("" + curvaSelecionada);
		
		fromClientePWM = in.readLine();
//		fromClienteEixoX = in.readLine();
		
//		while(fromClientePWM < ){
//			fromClientePWM = in.readLine();
//		}
		client.close();
		server.close();
	}

	public String getStatus() {
		return status;
	}
	
	public String getPWM(){
		return fromClientePWM;
	}
	
	public String getEixoX(){
		return fromClienteEixoX;
	}
	
	public void setCurvaSelecionada(int curva){
		curvaSelecionada = curva;
	}

}
