package com.isirius.minder.samples.xmlGenerator;

import com.isirius.minder.samples.xmlGenerator.xmlservices.XMLGenerationService;

public class XMLWorker implements Runnable {
  private boolean isTerminated;
  private XmlGeneratorAdapter xmlGeneratorAdapter;


  public XMLWorker(XmlGeneratorAdapter xmlGeneratorAdapter) {
    this.xmlGeneratorAdapter = xmlGeneratorAdapter;
    this.isTerminated = false;
  }

  protected void terminate() {
    isTerminated = true;
  }

  @Override
  public void run() {
    while (!isTerminated) {
      byte[] requestMessage = null;
      try {
        requestMessage = xmlGeneratorAdapter.take();
      } catch (InterruptedException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      XMLGenerationService xmlService = new XMLGenerationService();
      byte[] generatedXML = xmlService.generateXML(requestMessage);

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      System.out.println("Generated xml: ");
      System.out.println(new String(generatedXML));
      xmlGeneratorAdapter.xmlProduced(generatedXML);
    }
  }
}
