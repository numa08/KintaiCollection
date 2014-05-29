package net.numa08.kintaicollection.app.models;

import fj.Effect;

public class TaisyaReportRequest implements KintaiReportRequest{
    @Override
    public boolean request(Effect<Integer> progress) {
        return true;
    }
}
