package uy.edu.um.doors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessManagerImplTest {

    private ProcessManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new ProcessManagerImpl();
    }


    @Test
    void prepareProcesses() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();

        // Si pendientes no está vacío, executeNextProcess no debería tirar excepción
        try {
            manager.executeNextProcess();
        } catch (NotExistException e) {
            fail("Debería haber procesos pendientes después de prepareProcesses");
        }

        // Verificamos que el proceso con mayor prioridad
        assertEquals(58753, manager.getEnEjecucion().getPID());
    }

    @Test
    void executeNextProcess() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();

        try {
            manager.executeNextProcess();
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si hay procesos pendientes");
        }

        assertNotNull(manager.getEnEjecucion());
        assertEquals(58753, manager.getEnEjecucion().getPID());
    }

    @Test
    void finishProcessOk() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();
        manager.executeNextProcess();

        try {
            manager.finishProcessOk();
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si hay un proceso en ejecución");
        }

        assertNull(manager.getEnEjecucion());
    }

    @Test
    void finishProcessError() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();
        manager.executeNextProcess();

        try {
            manager.finishProcessError();
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si hay un proceso en ejecución");
        }

        assertNull(manager.getEnEjecucion());
    }

    @Test
    void terminateProcess() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();
        manager.executeNextProcess();

        try {
            manager.terminateProcess(25); // Zeus, ADMIN que existe en el CSV
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si hay proceso en ejecución y el usuario existe");
        }

        assertNull(manager.getEnEjecucion());
    }

    @Test
    void printStatus() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();
        manager.executeNextProcess();

        try {
            manager.printStatus();
        } catch (Exception e) {
            fail("printStatus no debería lanzar ninguna excepción");
        }
    }

    @Test
    void printStatusVerbose() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();
        manager.executeNextProcess();

        try {
            manager.printStatusVerbose();
        } catch (Exception e) {
            fail("printStatusVerbose no debería lanzar ninguna excepción");
        }
    }

    @Test
    void printStatusByUser() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();

        try {
            manager.printStatusByUser(25); // Zeus, existe en el CSV
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si el usuario existe");
        }
    }

    @Test
    void printStatusByProcess() {
        manager.loadProcessAndUserData("process.csv", "users.csv");
        manager.prepareProcesses();

        try {
            manager.printStatusByProcess(58753); // PID que existe en el CSV
        } catch (NotExistException e) {
            fail("No debería lanzar excepción si el proceso existe");
        }
    }
}