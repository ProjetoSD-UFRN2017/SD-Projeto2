import java.io.*;  
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RecebendoDados {

	private ServerSocket server;
	private Socket client;

	private String fromClientePWM;
	private double pwmValue;
	private String fromClienteEixoX;
	private double seg_value;
	private String status;

	private int curvaSelecionada;

	private boolean isConected;

	private int read;
	private byte[] buffer;
	private byte[] readData;

	public RecebendoDados() throws IOException {
		server = new ServerSocket(8015);
		status = "Servidor inciado, aguardando conexão...";

		isConected = false;
		fromClienteEixoX = "";
		fromClientePWM = "";
		status = "";
		curvaSelecionada = 0;
		read = -1;
	}

	public void startRetrievingData() throws IOException{

		client = server.accept();
		status = "Servidor conectado";
		isConected = true;

		PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		buffer = new byte[5*1024]; // a read buffer of 5KiB

		out.println("" + curvaSelecionada);

		String readDataText;
		int count = 1;
		while ((read = client.getInputStream().read(buffer)) > -1) {
			if(count == 1){ // valor do pwm
				readData = new byte[read];
				System.arraycopy(buffer, 0, readData, 0, read);
				readDataText = new String(readData,"UTF-8"); // assumption that client sends data UTF-8 encoded

				pwmValue = Double.parseDouble(readDataText);
				NumberFormat formato_pwm = new DecimalFormat("#0.00");     
				pwmValue = Double.parseDouble(formato_pwm.format(pwmValue));

				System.out.println("PWM: " + pwmValue); 
				fromClientePWM = "" + pwmValue;
			}
			else{ // valor do eixo x
				readData = new byte[read];
				System.arraycopy(buffer, 0, readData, 0, read);
				readDataText = new String(readData,"UTF-8"); // assumption that client sends data UTF-8 encoded

				seg_value = Double.parseDouble(readDataText);
				NumberFormat formato_seg = new DecimalFormat("#0");     
				seg_value = Double.parseDouble(formato_seg.format(seg_value));

				System.out.println("Segundos: " + seg_value); 
				fromClienteEixoX = "" + seg_value;

				count = 0;
			}
			count++;


		}

		status = "Secagem finalizada.";

		//client.close();
		//server.close();
		isConected = false;


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

	public void finalizaConexao() throws IOException{
		client.close();
		server.close();
		isConected = false;
	}

	public boolean getConectionStatus(){
		return isConected;
	}

}
