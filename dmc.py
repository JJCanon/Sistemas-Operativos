from pydub import AudioSegment as AS
import argparse as arg


if __name__=="__main__":
    
    
    #crear argumenparser para los parametros
    parser=arg.ArgumentParser(description="argumentos para la transformación")
    
    # Agregar argumentos
    parser.add_argument('-e',type=str,nargs='?',default=None,
                        help='entrada del -e que contiene el tipo de archivo al que se va a transformar el archivo')
    parser.add_argument('nombreFile',type=str,help='entrada del nombre del archivo a transformar')
    
    #Parsear parametros
    args=parser.parse_args()
    
    #Guardar argumentos en variables
    extension=args.e
    nameFile=args.nombreFile
    
    esCarpeta=False
    try:
        posicion=nameFile.index('.')
        esCarpeta=False
    except ValueError:
        esCarpeta=True
            
    if extension is not None and  esCarpeta :
        print("archivo: ",nameFile,"es una carpeta. pasar a ",extension)
        #funcion tranformar archivos de la carpeta en extensión
    elif(extension is not None and not esCarpeta):
        print("no es una carpeta. pasar a ",extension)
        #funcion pasar archivo a tipo extensión
    elif(extension is None and not esCarpeta):
        print("no es carpeta, se debe pasar a mp3,ogg,wav")
        #pasar archivo a los tres tipos de archivos mp3,ogg,wav
    elif(extension is None and esCarpeta):
        print("error")
        #es una carpeta pero no se especifica la extensión, marcar error
    
    #audio_input=AS.from_file("nombreFile")
    
    
    
    print(extension,nameFile)

    """
    parametros: -e=wav LaProcesiondelosBorrachos.mp3
    """
    
    
    """
    asuntos:
        1. paralelismo -> multiprocesing o threading
        2. funcion de lectura y transformacion de archivos
    librerias:
        a. manejo de archivos:
            1.pydub
            2.pyaifc
            3.OS
            4.Wave
            5.argparse
        b.paralelismo:
            1. Multiprocesing
            2. threading
        c. tiempos y tamaños
            1. Time
            2. OS
        d. posibles o candidatas
            1. logging -> NO
    """