int ledPins[] = {3,4,5,6,7}; // LED pins
int ledCnt = 5;

void setup()
{
   for(int p=0; p<=ledCnt; p++)
   {
       pinMode(ledPins[p], OUTPUT); // Set the mode to OUTPUT
   }
}

void loop()
{
   for(int p=0; p<=ledCnt; p++)   {
       analogWrite(ledPins[p], HIGH); // Turn an LED pin on
       
   }
}
