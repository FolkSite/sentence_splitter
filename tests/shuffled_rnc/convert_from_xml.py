#!/usr/bin/env python3

import os
import xml.sax

class SentenceXMLErrorHandler(xml.sax.ErrorHandler):
    def error(self, exception):
        print("Recoverable error: %s" % exception)

    def fatalError(self, exception):
        print("Non-recoverable error: %s, we begin producing output from this moment!" % exception)

    def warning(self, exception):
        print("Warning: %s" % exception)

class SentenceXMLContentHandler(xml.sax.ContentHandler):
    def __init__(self, outputBufferizer):
        self.se = ''
        self.ob = outputBufferizer

    def startElement(self, name, attrs):
        if name == 'se':
            self.se = ''
            self.start = True
        elif name == 'w':
            if not self.start:
                self.se += ' '
            else:
                self.start = False

    def characters(self, content):
        self.se += content.replace('`', '').replace("\n", "").replace(' ', '')

    def endElement(self, name):
        if name == 'se':
            self.ob.addSentence(self.se)
            self.se = ''

class OutputBufferizer(object):
    def __init__(self):
        self.buffer = ""

    def addSentence(self, s):
        self.buffer += s + "\n"

    def writeOutput(self, fileName):
        with open(fileName, "w") as f:
            f.write(self.buffer)

def convertFile(inputFileName, outputFileName):
    parser = xml.sax.make_parser()
    outputBufferizer = OutputBufferizer()
    parser.setContentHandler(SentenceXMLContentHandler(outputBufferizer))
    parser.setErrorHandler(SentenceXMLErrorHandler())
    parser.parse(inputFileName)
    outputBufferizer.writeOutput(outputFileName)

if __name__ == "__main__":
    for fileName in os.listdir('.'):
        if os.path.isfile(fileName) and fileName[-4:] == ".xml":
            convertFile(fileName, fileName[:-4] + ".txt")
