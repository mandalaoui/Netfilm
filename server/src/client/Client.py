import socket
import sys

# Client class to manage the connection and communication with the server.
class Client:
    # Initialize the client with the server's port and IP address.
    def __init__(self, port: int, ip_dest: str):
            self.port = port
            self.ip_dest = ip_dest

    # Function to handle the client's communication loop.
    def runClient(self):
            # Create a socket using IPv4 and TCP and connect to the server.
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((self.ip_dest, self.port))
            # Infinite loop to interact with the user and communicate with the server.
            while True:
                # Take user input to send as a command to the server.
                msg = input()
                s.send(bytes(msg, 'utf-8'))
                res = s.recv(4096)
                print(res.decode('utf-8'))
            s.close()


def main():
    # arguments were passed via the command line, first argument: IP address, second argument: port number.
    if len(sys.argv) > 1:
        ip_dest = sys.argv[1]
    if len(sys.argv) > 2:
        port = int(sys.argv[2])


    # Create a Client instance with the provided IP and port.
    client_instance = Client(port, ip_dest)
    # Start the client
    client_instance.runClient()

# Entry point for the program
if __name__ == "__main__":
    main()