#include <iostream>
#include <thread>
#include <vector>

using namespace std;

// Función que será ejecutada por cada hilo
void myThreadFunction(int initialValue)
{
    // Transformar el valor recibido
    cout << "Resultado del hilo: " << initialValue + 1 << endl;
}

int main()
{
    const int numThreads = 3;
    vector<thread> threads;

    // Valor inicial compartido por todos los hilos
    int initialValue = 1;

    // Crear un vector de hilos
    for (int i = 0; i < numThreads; ++i)
    {
        // Crear un hilo y pasar el valor inicial como argumento
        threads.emplace_back(myThreadFunction, initialValue + i);
    }

    // Esperar a que todos los hilos terminen
    for (auto &thread : threads)
    {
        thread.join();
    }

    return 0;
}
