package com.sequenceiq.cloudbreak.cloud.aws.view;

import com.sequenceiq.cloudbreak.cloud.model.Volume;

public class AwsVolumeView {

    private static final char[] DEVICE_CHAR = {
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private Volume volume;

    private String device;

    private int index;

    public AwsVolumeView(Volume volume, int index) {
        this.volume = volume;
        this.device = volume.getMount() + DEVICE_CHAR[index];
    }

    public int getIndex() {
        return index;
    }

    public String getMount() {
        return volume.getMount();
    }

    public String getType() {
        return volume.getType();
    }

    public int getSize() {
        return volume.getSize();
    }

    public String getDevice() {
        return device;
    }
}