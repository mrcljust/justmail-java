package src.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloseableUtil
{
    public static void close(Closeable p_closeable)
    {
        if (p_closeable != null)
        {
            try
            {
                p_closeable.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(CloseableUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
