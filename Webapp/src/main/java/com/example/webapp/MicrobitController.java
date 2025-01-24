package com.example.webapp;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class MicrobitController {
    public static void main(String[] args) {
        SerialPort comPort = null;
        while (comPort == null || !comPort.isOpen()) {
            SerialPort[] ports = SerialPort.getCommPorts();
            if (ports.length == 0) {
                System.out.println("No serial ports available.");
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            comPort = ports[0]; // Adjust this to select the correct port
            System.out.println("Selected port: " + comPort.getSystemPortName());

            if (comPort.isOpen()) {
                comPort.closePort();
                System.out.println("Port closed.");
            }

            if (comPort.openPort()) {
                System.out.println("Port opened.");
            } else {
                System.out.println("Failed to open port. Error: " + comPort.getLastErrorCode());
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        try {
            Robot robot = new Robot();
            System.out.print("help");
            while (true) {
                try {
                    if (comPort.bytesAvailable() > 0) {
                        System.out.println("Bytes available: " + comPort.bytesAvailable());
                        byte[] readBuffer = new byte[comPort.bytesAvailable()];
                        comPort.readBytes(readBuffer, readBuffer.length);
                        System.out.println("Read bytes: " + new String(readBuffer));

                        for (byte b : readBuffer) {
                            switch (b) {
                                case 'a':
                                    System.out.println("Received 'a'");
                                    robot.keyPress(KeyEvent.VK_UP);
                                    robot.keyRelease(KeyEvent.VK_UP);
                                    break;
                                case 'b':
                                    System.out.println("Received 'b'");
                                    robot.keyPress(KeyEvent.VK_DOWN);
                                    robot.keyRelease(KeyEvent.VK_DOWN);
                                    break;
                                case 's':
                                    System.out.println("Received 's'");
                                    robot.keyPress(KeyEvent.VK_SPACE);
                                    robot.keyRelease(KeyEvent.VK_SPACE);
                                    break;
                                default:
                                    System.out.println("Received unknown byte: " + b);
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Bytes available: " + comPort.bytesAvailable());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            comPort.closePort();
            System.out.println("Port closed.");
        }
    }
}
