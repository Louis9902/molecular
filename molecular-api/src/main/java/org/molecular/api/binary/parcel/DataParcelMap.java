/*
 * This file ("DataParcelMap.java") is part of the molecular-project by Louis.
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

package org.molecular.api.binary.parcel;

import org.molecular.api.binary.DataCluster;
import org.molecular.api.binary.io.DataParcelReader;
import org.molecular.api.binary.io.DataParcelWriter;
import org.molecular.api.util.APIInternal;

import javax.annotation.Nonnull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

/**
 * @author Louis
 */

public class DataParcelMap extends BaseDataParcel<DataCluster> {

    @APIInternal
    public DataParcelMap(@Nonnull DataInput input) throws IOException {
        super(input);
    }

    public DataParcelMap(@Nonnull String name, @Nonnull DataCluster data) {
        super(name, data);
    }

    @Override
    public void write(@Nonnull DataOutput output) throws IOException {
        output.writeInt(this.data.size());
        for (Map.Entry<String, BaseDataParcel<?>> entry : this.data) {
            DataParcelWriter.writeParcel(entry.getValue(), output);
        }
    }

    @Override
    public void read(@Nonnull DataInput input) throws IOException {
        this.data = new DataCluster();
        int size = input.readInt();
        for (int i = 0; i < size; i++) {
            this.data.add(DataParcelReader.readParcel(input));
        }
    }

    @Override
    public Class<DataCluster> getDataType() {
        return DataCluster.class;
    }
}
