/*
 * This file ("DataParcelReader.java") is part of the molecular-project by Louis.
 * Copyright © 2017 Louis
 *
 * The molecular-project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The molecular-project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with molecular-project.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.molecular.api.binary.io;

import org.molecular.api.Molecular;
import org.molecular.api.binary.parcel.BaseDataParcel;
import org.molecular.api.util.APIInternal;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Louis
 */

public class DataParcelReader implements Closeable {

    private final DataInputStream stream;

    public DataParcelReader(InputStream stream) {
        this.stream = stream instanceof DataInputStream ? (DataInputStream) stream : new DataInputStream(stream);
    }

    @APIInternal
    public static BaseDataParcel<?> readParcel(@Nonnull DataInput input) throws IOException {
        byte index = input.readByte();
        Class<? extends BaseDataParcel> clazz = Molecular.DATA_PART_REGISTRY.getValueSafe((int) index);
        try {
            return clazz.getConstructor(DataInput.class).newInstance(input);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IOException("Unable to create instance of class " + clazz, e);
        } catch (NoSuchMethodException e) {
            throw new IOException("No matching constructor with DataInput found for " + clazz, e);
        }
    }

    public BaseDataParcel<?> readParcel() throws IOException {
        return DataParcelReader.readParcel(this.stream);
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }
}
