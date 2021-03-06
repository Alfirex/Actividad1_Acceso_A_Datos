package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el n�mero de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila m�s
		}

		// dimensionar la matriz contenido seg�n los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzar� una excepci�n
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Error. No tienes permisos para crear la carpeta");
		}
		//que no exista -> lanzar� una excepci�n
		if(file.exists()) {
			throw new GestionFicherosException("Error. La carpeta ya existe.");
		}
		//crear la carpeta -> lanzar� una excepci�n
		if(!file.mkdir()) {
			throw new GestionFicherosException("Error. No se ha podido crear la carpeta.");
		}
		
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		// TODO Auto-generated method stub
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Error. No tienes permisos para crear el fichero");
		}
		//que no exista -> lanzar� una excepci�n
		if(file.exists()) {
			throw new GestionFicherosException("Error. El fichero ya existe.");
		}
		//crear la carpeta -> lanzar� una excepci�n
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new GestionFicherosException("Error. El fichero ya existe.");
		}
		actualiza();

	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo, arg0);
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("Error. No tienes permisos para eliminar");
		}
		if(file.exists()) {
			file.delete();
		}
		else {
			throw new GestionFicherosException("Error. El elemento que quieres eliminar no existe.");
		}
		actualiza();
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignaci�n de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzar� una excepci�n
		if(!file.exists()) throw new GestionFicherosException("El fichero no existe.");
		//Controlar que haya permisos de lectura. Si no, se lanzar� una excepci�n
		if(!file.canRead()) throw new GestionFicherosException("En este fichero no hay permisos de lectura.");	
		
		//T�tulo
		strBuilder.append("INFORMACI�N DEL SISTEMA");
		strBuilder.append("\n\n");
		
		//Nombre
		strBuilder.append("Nombre: ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo: fichero o directorio
		if(file.isDirectory()) {// En el caso que sea un Directorio
			strBuilder.append("Es un directorio."+"\n");
			//Si es directorio: N�mero de elementos que contiene.
			File[] listadoDelDirectorio = file.listFiles();
			int contador = 0;
			while(contador<listadoDelDirectorio.length) {
				contador++;
			}
			strBuilder.append("La cantidad de ficheros del directorio es: "+ contador +"\n");
			 
			//Si es directorio: Espacio libre, espacio disponible, espacio total (bytes)
			strBuilder.append("Espacio Libre"+"\n");
			strBuilder.append(file.getFreeSpace()+" Bytes"+"\n");
			strBuilder.append("Espacio Disponible"+"\n");
			strBuilder.append(file.getUsableSpace()+" Bytes"+"\n");
			strBuilder.append("Espacio total"+"\n");
			strBuilder.append(file.getTotalSpace()+" Bytes"+"\n");			
							
		}else {//En el caso que sea un fichero
			strBuilder.append("Es un fichero."+"\n");
		//Si es un fichero: Tama�o en bytes
			strBuilder.append("Los bytes:."+ file.length()+"\n");
		}
		//Ubicaci�n
		strBuilder.append("La ubicacion del fichero"+"\n");
		strBuilder.append(file.getAbsolutePath()+"\n");
		
		//Fecha de �ltima modificaci�n
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mmm:ss");//Declaramos una variable de tipo SimpledateFormat para dar el formato para que de fecha 
		strBuilder.append("Ultima modificacion");
		strBuilder.append( sdf.format( file.lastModified() )+"\n" );
		
		//Si es un fichero oculto o no
		
		if(file.isHidden()) {
			strBuilder.append("Esta Oculto."+"\n");
		}else {
			strBuilder.append("No esta Oculta."+"\n");
		}
				
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo, arg0);
		File newFile = new File(carpetaDeTrabajo, arg1);
		if(newFile.exists()) {
			throw new GestionFicherosException("El nombre del fichero ya existe");
		}
		if (!file.exists()) {
			throw new GestionFicherosException("Error. No se ha encontrado el fichero");
		}else {
			 boolean result = file.renameTo(newFile);
		}
		
		actualiza();
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la direcci�n exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
