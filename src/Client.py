import socket

class Client:
    def __init__(self, port: int, ip_dest: int):
            self.port = port
            self.ip_dest = ip_dest

    def runClient(self):
        # try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((self.ip_dest, self.port))
            print(f"Connected to server at {self.ip_dest}:{self.port}")

            while True:
                msg = input()
                s.send(bytes(msg, 'utf-8'))
                res = s.recv(4096)
                print(res.decode('utf-8'))

            s.close()
        # except Exception as e:
        #     print("An error occurred:", e)


def main():
    client_instance = Client(12345, "127.0.0.1")
    client_instance.runClient()

if __name__ == "__main__":
    main()