package net.numa08.kintaicollection.app.models;

import fj.Effect;

public interface KintaiReportRequest {

    public boolean request(Effect<Integer> progress);

}
