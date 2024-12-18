import socket
import sys

class Client:
    def __init__(self, port: int, ip_dest: str):
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
                # res = b""
                # while True:
                #     chunk = s.recv(4096) 
                #     if not chunk:
                #         break
                #     res += chunk
                #     if b"\n" in res:  
                #         break                
                print(res.decode('utf-8'))
            s.close()
        # except Exception as e:
        #     print("An error occurred:", e)


def main():
    port = 12345  # ברירת מחדל אם לא הועבר ערך מהשורת פקודה
    ip_dest = "127.0.0.1"

    # בדיקה אם הועברו ארגומנטים לפורט ו-IP
    if len(sys.argv) > 1:
        ip_dest = sys.argv[1]
    if len(sys.argv) > 2:
        port = int(sys.argv[2])



    client_instance = Client(port, ip_dest)
    client_instance.runClient()

if __name__ == "__main__":
    main()