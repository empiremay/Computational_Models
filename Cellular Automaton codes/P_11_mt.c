#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX_SIZE 1024

void q0(char *, int *);
void q1(char *, int *);
void q2(char *, int *);
void q3(char *, int *);
void initAutomata(char *, int *);

void q0(char *cad, int *p)
{
	fprintf(stdout, "Q0 cad[%d] - %s\n", *p, cad);
	if(cad[*p] == 'B')
		q1(cad, p);
}

void q1(char *cad, int *p)
{
	fprintf(stdout, "Q0 cad[%d] - %s\n", *p, cad);
	(*p)++;
	if(cad[*p] == '1')
		q2(cad, p);
	else if(cad[*p] == 'B')
		q3(cad, p);
}

void q2(char *cad, int *p)
{
	fprintf(stdout, "Q0 cad[%d] - %s\n", *p, cad);
	(*p)++;
	if(cad[*p] == '1')
		q1(cad, p);
	else if(cad[*p] == 'B')
		q2(cad, p);
}

void q3(char *cad, int *p)
{
	fprintf(stdout, "Terminado. Es una cadena par\n");
	exit(2);
}

void initAutomata(char *cad, int *p)
{
	if(*p < strlen(cad))
		if(cad[0] == 'B')
			q0(cad, p);
		else
			fprintf(stderr, "Cadena no aceptada\n");
	exit(1);
}

int main()
{
	fprintf(stdout, "Si no termina, la cadena es impar\n");
	int posicion=0;
	initAutomata("B1111B", &posicion);
	return 0;
}
