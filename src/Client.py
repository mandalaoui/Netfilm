import socket

class Client:
    def __init__(self, port: int, ip_dest: int):
            self.port = port
            self.ip_dest = ip_dest

    def runClient():
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((self.ip_dest, self.port))

        while true:
            msg = input()
            s.send(bytes(msg, 'utf-8'))
            res = s.recv(4096)
            print(res.decode('utf-8'))

        s.close()