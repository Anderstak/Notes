package protocol.model;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Сообщение об ошибке в RESP протоколе
 */
public class RespError implements RespObject {

    /**
     * Код объекта
     */
    public static final byte CODE = '-';

    private byte[] message;

    public RespError(byte[] message) {
        this.message = message;
    }

    /**
     * Ошибка ли это? Ответ - да
     *
     * @return true
     */
    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public String asString() {
        if (isEmpty()) {
            return null;
        } else {
            return new String(message);
        }
    }

    @Override
    public void write(OutputStream os) throws IOException {
        String r = (char)CODE + new String(message) + "\r\n";
        os.write(r.getBytes());
    }

    private boolean isEmpty() {
        return (message == null) || (message.length == 0);
    }
}
