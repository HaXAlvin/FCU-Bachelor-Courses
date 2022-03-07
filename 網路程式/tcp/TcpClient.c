#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 4444
#define IP_ADDRESS "140.134.26.200"

int main(){

	int clientSocket, checkConnect;
	struct sockaddr_in serverAddr;
	char buffer[1024];
	
	/*create socket*/

	/*create socket*/

	memset(&serverAddr, '\0', sizeof(serverAddr));//initial

	/*set serversocket struct IPv4, Port, Ip ...*/

	/*set serversocket struct IPv4, Port, Ip ...*/

	/*connect*/

	/*connect*/

	while(1){
		printf("Client : ");
		scanf("%s", &buffer[0]);
		/*send data to server*/

		/*send data to server*/
		if(strcmp(buffer, ":exit") == 0){
			close(clientSocket);
			printf("[-]Disconnected from server.\n");
			exit(1);
		}

		if(recv(clientSocket, buffer, 1024, 0) < 0){
			printf("[-]Error in receiving data.\n");
		}else{
			printf("Server : %s\n", buffer);
		}
	}

	return 0;
}
