package uy.edu.um.doors;

import uy.edu.um.tad.hash.MyHash;
import uy.edu.um.tad.hash.MyHashImpl;
import uy.edu.um.tad.heap.MyHeap;
import uy.edu.um.tad.heap.MyHeapImpl;
import uy.edu.um.tad.queue.EmptyQueueException;
import uy.edu.um.tad.queue.MyQueue;
import uy.edu.um.tad.queue.MyQueueImpl;
import uy.edu.um.tad.stack.MyStack;
import uy.edu.um.tad.stack.MyStackImpl;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessManagerImpl implements ProcessManager{

    //EL DISEÑO DE LA ESTRUCTURA DE ALMACENAMIENTO DEBE IMPLEMENTARSE EN ESTA CLASE EN RELACIÓN CON LAS ENTIDADES QUE DEFINA

    private MyQueue<Proceso> colaNuevos;
    private MyHeap<Proceso> pendientes;
    private Proceso enEjecucion;
    private MyStack<Proceso> finalizados;
    private MyHash<Integer, Usuario> usuarios;

    private BufferedWriter log;

    public ProcessManagerImpl() {
        this.colaNuevos = new MyQueueImpl<>();
        this.pendientes = new MyHeapImpl<>();
        this.finalizados = new MyStackImpl<>();
        this.usuarios = new MyHashImpl<>();
        this.enEjecucion = null;
        String nombreLog = "DOORS_PROCESS_LOG_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        try {
            this.log = new BufferedWriter(new FileWriter(nombreLog, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo auxiliar
    private void escribirLog(String mensaje) {
        try {
            log.write(mensaje);
            log.newLine();
            log.flush();
        } catch (Exception e) {
            System.out.println("Error escribiendo en el log: " + e.getMessage());
        }
    }

    @Override
    public void loadProcessAndUserData(String processCsvPath, String usersCsvPath) {
        // PARTE 1: cargar usuarios
        try {
            BufferedReader readerUsuarios = new BufferedReader(new FileReader(usersCsvPath));
            String linea = readerUsuarios.readLine(); // saltea el encabezado
            while ((linea = readerUsuarios.readLine()) != null) {
                String[] partes = linea.split(";");
                int uid = Integer.parseInt(partes[0]);
                String alias = partes[1];
                TipoUsuario tipo = TipoUsuario.valueOf(partes[2]);
                Usuario usuario = new Usuario(uid, alias, tipo);
                usuarios.put(uid, usuario);
            }
            readerUsuarios.close();
        } catch (Exception e) {
            System.out.println("Error leyendo usuarios: " + e.getMessage());
        }

        // PARTE 2: cargar los procesos
        try {
            BufferedReader readerProcesos = new BufferedReader(new FileReader(processCsvPath));
            String linea = readerProcesos.readLine(); // saltea el encabezado
            while ((linea = readerProcesos.readLine()) != null) {
                String[] partes = linea.split(";");
                int pid = Integer.parseInt(partes[0]);
                int uid = Integer.parseInt(partes[1]);
                String nombre = partes[2];
                Usuario usuario = usuarios.get(uid);
                Proceso proceso = new Proceso(pid, nombre, usuario);
                // parsear eventos
                String eventosStr = partes[3];
                extraerEventos(eventosStr, proceso);
                colaNuevos.enqueue(proceso);
            }
            readerProcesos.close();
        } catch (Exception e) {
            System.out.println("Error leyendo procesos: " + e.getMessage());
        }

    }

    private void extraerEventos(String eventosStr, Proceso proceso) {
        // sacar las llaves { }
        eventosStr = eventosStr.replace("{", "").replace("}", "");

        // separar cada evento por #
        String[] eventos = eventosStr.split("#");

        for (String eventoStr : eventos) {
            eventoStr = eventoStr.trim();

            // separar tipo de instrucciones: "DISK:[commit, fsync]"
            String[] partes = eventoStr.split(":\\[");
            String tipo = partes[0].trim();
            String instruccionesStr = partes[1].replace("]", "");

            Evento evento = new Evento(TipoEvento.valueOf(tipo));

            // separar instrucciones por coma
            String[] instrucciones = instruccionesStr.split(",");
            for (String instruccion : instrucciones) {
                evento.getInstrucciones().add(instruccion.trim());
            }

            proceso.getEventos().add(evento);
        }
    }

    @Override
    public void prepareProcesses() {

        //Calculamos la prioridad de cada proceso en la cola de nuevos procesos

        while (! colaNuevos.isEmpty()){

            try {
                Proceso procesoActual = colaNuevos.dequeue();

                //Contamos en un proceso cuantos son de cada tipo
                int contadorCPU = 0;
                int contadorRAM = 0;
                int contadorDISK = 0;


                for(int i =0; i< procesoActual.getEventos().size(); i++){
                    if( procesoActual.getEventos().get(i).getTipo() == TipoEvento.CPU ){
                        contadorCPU++;
                    }
                    else if( procesoActual.getEventos().get(i).getTipo() == TipoEvento.RAM){
                        contadorRAM++;
                    }
                    else{
                        contadorDISK++;
                    }
                }

                int total = contadorCPU + contadorRAM + contadorDISK;
                Usuario usuarioAsociado = procesoActual.getUsuario();
                int peso = 0;

                if(usuarioAsociado.getTipo() == TipoUsuario.ADMIN){
                    peso = 32;
                }
                else {
                    peso = 16;
                }
                int prioridad = ( (8*contadorCPU + 2*contadorRAM + 2*contadorDISK)/total ) + (peso * total);

                procesoActual.setPrioridad(prioridad);
                procesoActual.setEstado(EstadoProceso.PENDING);

                escribirLog("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]: NEW PENDING PROCESS: PID=" + procesoActual.getPID() + " | " + procesoActual.getNombre() + " | USER:" + procesoActual.getUsuario().getAlias() + " UID:" + procesoActual.getUsuario().getUID() + " | P=" + procesoActual.getPrioridad());

                pendientes.insert(procesoActual);

            } catch (EmptyQueueException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public void executeNextProcess() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void finishProcessOk() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void finishProcessError() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void terminateProcess(int uid) {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatus() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusVerbose() {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusByUser(int uid) {
        System.out.println("IMPLEMENTAR");
    }

    @Override
    public void printStatusByProcess(int pid) {
        System.out.println("IMPLEMENTAR");
    }
}
