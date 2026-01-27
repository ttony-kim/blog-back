package project.blog.global.config.common;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadableRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public ReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String contentType = request.getContentType();

        if (contentType != null && (contentType.contains("multipart") || contentType.contains("image"))) {
            this.cachedBody = new byte[0];
        } else {
            InputStream inputStream = request.getInputStream();
            this.cachedBody = StreamUtils.copyToByteArray(inputStream);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedServletInputStream(this.cachedBody);
    }

    public byte[] getContentAsByteArray() {
        return this.cachedBody;
    }

    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {}

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

    }

}
