package ch.chrestawilli.notifyourself;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageStore {
    private File storageDir;
    private File storageFile;

    public class MessageStoreException extends RuntimeException {
        public MessageStoreException(String message) {
            super(message);
        }
    }

    public MessageStore(File storageDir) {
        this.storageDir = storageDir;
        storageFile = new File(storageDir, "message_store");
    }

    public void store(List<Message> messages) {
        try {
            storageFile.createNewFile();
        } catch (IOException e) {
            throw new MessageStoreException(e.getMessage());
        } catch (SecurityException e) {
            throw new MessageStoreException(e.getMessage());
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(storageFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(messages);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new MessageStoreException(e.getMessage());
        } catch (IOException e) {
            throw new MessageStoreException(e.getMessage());
        }
    }

    public List<Message> load() {
        try {
            FileInputStream fileInputStream = new FileInputStream(storageFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            ArrayList<Message> messageList = (ArrayList<Message>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();

            return messageList;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        } catch (ClassCastException e) {
            // There was a read error; delete messages
            storageFile.delete();
            return new ArrayList<>();
        }
    }
}
