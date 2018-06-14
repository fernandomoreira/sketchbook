#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

const int tamanho = 70;

int main()
{


    int fd = open("/dev/ttyUSB0", O_RDWR | O_NOCTTY | O_NDELAY);
struct termios options;
tcgetattr(fd, &options);
cfsetispeed(&options, B9600);
cfsetospeed(&options, B9600);
options.c_cflag |= (CLOCAL | CREAD);
tcsetattr(fd, TCSANOW, &options);
    int n;
    int valor;
    for (n=1;;n++)

    {
     FILE *porta = fopen("/dev/ttyUSB0", "w+");
     char caracter[1] = "1";
     fwrite(&caracter, sizeof(caracter), 1, porta);
     usleep(1000000);
     char valores[tamanho];
     fgets(valores, tamanho, porta);
     fclose(porta);
     printf("%s", valores);
     FILE *sensores = fopen("sensores.log", "w");
     fputs(valores, sensores);
     fclose(sensores);
     }
}
