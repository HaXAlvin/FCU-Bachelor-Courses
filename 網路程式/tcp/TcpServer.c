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

	int serversocket, checkbind;
	struct sockaddr_in serverAddr;

	int newSocket;
	struct sockaddr_in newAddr;

	socklen_t addr_size;

	char buffer[1024];
	pid_t childpid;

	/*create socket*/

	/*create socket*/
	

	memset(&serverAddr, '\0', sizeof(serverAddr));// initial

	/*set socket struct IPv4, Port, Ip ...*/

	/*set socket struct IPv4, Port, Ip ...*/
	

	/*bind server address*/

	/*bind server address*/


	if(listen(serversocket, 10) == 0){
		printf("[+]Listening....\n");
	}else{
		printf("[-]Error in binding.\n");
	}


	while(1){
		newSocket = accept(serversocket, (struct sockaddr*)&newAddr, &addr_size);
		if(newSocket < 0){
			exit(1);
		}
		printf("Connection accepted from %s:%d\n", inet_ntoa(newAddr.sin_addr), ntohs(newAddr.sin_port));

		if((childpid = fork()) == 0){
			close(serversocket);

			while(1){
				/*store data from newsocket into buffer*/

				/*store data from newsocket into buffer*/
				if(strcmp(buffer, ":exit") == 0){
					printf("Disconnected from %s:%d\n", inet_ntoa(newAddr.sin_addr), ntohs(newAddr.sin_port));
					break;
				}else{
					printf("Client : %s\n", buffer);
					send(newSocket, buffer, strlen(buffer), 0);
					bzero(buffer, sizeof(buffer));//clean buffer
				}
			}
		}else if(childpid > 0){
			printf("Still listening ...\n");
		}

	}

	close(newSocket);


	return 0;
}
