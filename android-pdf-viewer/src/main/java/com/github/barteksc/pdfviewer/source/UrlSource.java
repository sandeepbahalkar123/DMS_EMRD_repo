/*
 * Copyright (C) 2016 Bartosz Schiller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.IOException;

public class UrlSource implements DocumentSource {
    private File file;
    private String url;

    public UrlSource(String url) {
        this.url = url;
    }

    @Override
    public PdfDocument createDocument(Context context, PdfiumCore core, String password) throws IOException {
        final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/";
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index);
        file = new File(SDPath, fileName);
        if (file.exists()) {
            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            return core.newDocument(pfd, password);
        }
        return null;

    }
}