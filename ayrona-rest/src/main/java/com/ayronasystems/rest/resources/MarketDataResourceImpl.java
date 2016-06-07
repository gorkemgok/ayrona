package com.ayronasystems.rest.resources;

import com.ayronasystems.core.Singletons;
import com.ayronasystems.core.batchjob.BatchJobManager;
import com.ayronasystems.core.batchjob.impl.ImportMarketDataBatchJob;
import com.ayronasystems.core.timeseries.moment.Bar;
import com.ayronasystems.rest.bean.ErrorBean;
import com.ayronasystems.rest.resources.definition.MarketDataResource;
import com.mongodb.MongoClient;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 04/06/16.
 */
public class MarketDataResourceImpl implements MarketDataResource {

    private static Logger log = LoggerFactory.getLogger (MarketDataResourceImpl.class);

    public static final SimpleDateFormat SDF = new SimpleDateFormat ("yyyyMMddHHmm");

    private BatchJobManager batchJobManager = Singletons.INSTANCE.getBatchJobManager ();

    private MongoClient mongoClient = Singletons.INSTANCE.getMongoClient ();

    public Response importCsv (MultipartFormDataInput input, String symbol) {
        List<Bar> barList = new ArrayList<Bar> (1000);
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        int i = 0;
        for (InputPart inputPart : inputParts) {

            try {
                InputStreamReader inputStreamReader = new InputStreamReader (
                        inputPart.getBody (InputStream.class, null));
                BufferedReader br = new BufferedReader (inputStreamReader);
                String line;
                while ( (line = br.readLine ()) != null ) {
                    String[] value = line.split (",");
                    Date date = SDF.parse (value[1]);
                    double open = Double.valueOf (value[2]);
                    double high = Double.valueOf (value[3]);
                    double low = Double.valueOf (value[4]);
                    double close = Double.valueOf (value[5]);
                    double volume = Double.valueOf (value[6]);
                    barList.add (
                            new Bar (date, open, high, low, close, volume)
                    );
                }

            } catch ( Exception e ) {
                log.error ("Cant handle imported file:", e);
                return Response.status (Response.Status.INTERNAL_SERVER_ERROR)
                               .entity (new ErrorBean (32423, e.getMessage ()))
                               .build ();
            }
        }
        ImportMarketDataBatchJob importMarketDataBatchJob = new ImportMarketDataBatchJob (symbol, barList, mongoClient);
        String id = batchJobManager.start (importMarketDataBatchJob);
        return Response.ok (id).build ();
    }
}
