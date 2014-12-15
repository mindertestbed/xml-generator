package wrapper;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import minderengine.MinderException;
import minderengine.Signal;
import minderengine.Slot;
import minderengine.Wrapper;
import xmlservices.XMLGenerationService;

public abstract class XmlGeneratorWrapper extends Wrapper {

  private boolean isRunning = false;
  private ArrayList<XMLWorker> xmlWorkerList;
  private LinkedBlockingQueue<byte[]> requestQueue;
  private final int NUMBER_OF_WORKER_THREAD = 5;

  public byte[] take() throws InterruptedException {
    return requestQueue.take();
  }

  @Override
  public void startTest() {
    isRunning = true;

    requestQueue = new LinkedBlockingQueue<byte[]>();

    xmlWorkerList = new ArrayList<XMLWorker>();
    for (int i = 0; i < NUMBER_OF_WORKER_THREAD; i++) {
      XMLWorker xmlWorker = new XMLWorker(this);
      Thread thread = new Thread(xmlWorker);
      thread.start();
      xmlWorkerList.add(xmlWorker);
    }
  }

  @Override
  public void finishTest() {
    isRunning = false;

    for (int i = 0; i < xmlWorkerList.size(); i++) {
      xmlWorkerList.get(i).terminate();
    }
  }

  @Signal
  public abstract void xmlProduced(byte[] generatedXML);


  @Slot
  public void generateXML(byte[] inputBooks) {
    if (!isRunning)
      throw new MinderException(MinderException.E_SUT_NOT_RUNNING);

    requestQueue.add(inputBooks);
  }

  @Override
  public String getLabel() {
    return "xmlGenerator";
  }
}
