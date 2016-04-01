/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package ryan.com.librarybase.net.http;

import java.net.URI;

import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;

/**
 * The current Android (API level 21) bundled version of the Apache Http Client does not implement
 * a HttpEntityEnclosingRequestBase type of HTTP DELETE method.
 * Until the Android version is updated this can serve in it's stead.
 * This implementation can and should go away when the official solution arrives.
 */
public final class HttpDelete extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "DELETE";

    public HttpDelete() {
        super();
    }

    /**
     * @param uri target url as URI
     */
    public HttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * @param uri target url as String
     * @throws IllegalArgumentException if the uri is invalid.
     */
    public HttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
