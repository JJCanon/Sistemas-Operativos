#Librerías
from pydub import AudioSegment as AS #Manejos de Audio
import argparse as arg #Manejo de parametro desde terminal
import os # Manejo de Archivos
import aifc # Manejo de Archivos de tipo AIF 


# Variables Globales

#Funciones

#Funcion para ver los archivos de una carpeta
def archivosCarpeta(nameFolder):
    
    # Listar todos los archivos en el directorio (la carpeta debe estar en la misma ruta del programa)
    archivos = os.listdir(nameFolder)

    return archivos

#Funcion para transformar archivo aiff a mp3(funcion temporal)
def aiff_mp3(archivoAif,extension):
    # Cargar archivo aiff
    audioAiff=AS.from_file(archivoAif,format="aiff")
    # Transformar archivo a mp3
    audioAiff.export("cancionMp3.mp3",format=extension)

#funcion para transformar archivos aiff a wav(funcion temporal)
def aiff_wav(archivoAif,extension):
    #cargar archivo aiff
    audioAiff=AS.from_file(archivoAif,format="aiff")
    #transformar el archivo a wav
    audioAiff.export("cancionWav.wav",format=extension)

# Funcion que transforma los archivos en el formato especificado
def transformarAudio(nameFile,extension=None):
    # separar nombre y formato del archivo de entrada
    name_extension=nameFile.split('.')
    # Cargar archivo
    audio=AS.from_file(nameFile,format=name_extension[1])
    # exportar archivo en formato especificado
    audio.export((name_extension[0]+"1."+extension),format=extension)
    

#Main
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
    
    #verificar si el archivo es o no es una carpeta
    esCarpeta=False
    try:
        posicion=nameFile.index('.')
        esCarpeta=False
    except ValueError:
        esCarpeta=True
    
    #transformar audio
    
    """aiff_mp3(nameFile,extension)
    aiff_wav(nameFile,extension)"""
    transformarAudio(nameFile,extension) 
            
    if extension is not None and  esCarpeta : 
        print("archivo: ",nameFile,"es una carpeta. pasar a ",extension)
        #buscar archivos de la carpeta
        archivos=archivosCarpeta(nameFile)
        # Mostrar los nombres de los archivos
        for archivo in archivos:
            print(archivo)
        #funcion tranformar archivos de la carpeta en extensión   
    #
    elif(extension is not None and not esCarpeta):
        print("no es una carpeta. pasar a ",extension)
        #funcion pasar archivo a tipo extensión
    #
    elif(extension is None and not esCarpeta):
        print("no es carpeta, se debe pasar a mp3,ogg,wav")
        #pasar archivo a los tres tipos de archivos mp3,ogg,wav
    #
    elif(extension is None and esCarpeta):
        print("error")
        #es una carpeta pero no se especifica la extensión, marcar error
    
    
    



    """
    parametros: 
        python dmc.py -e=wav LaProcesiondelosBorrachos.mp3
        python dmc.py -e=mp3 "01 What Makes You Beautiful.aif"
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