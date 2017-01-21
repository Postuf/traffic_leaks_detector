package detector.ThreatPattern.PatternParser;

import detector.LogHandler;
import detector.ThreatPattern.ThreatPattern;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * Created by SAMSUNG on 20.01.2017.
 */
public class PatternsDbParser extends ResourcePatternParser
{

    @Override
    protected InputStream getPatternDataInputStream()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resStream = classLoader.getResourceAsStream("patterns.json");
        return resStream;
    }


    @Override
    public void fillListWithData(List<ThreatPattern> listToFill)
    {
        try
        {
            JSONObject json = new JSONObject(getResourceData());
            for (String name : json.keySet())
            {
                JSONObject jsonPattern = json.getJSONObject(name);
                if(!jsonPattern.has("priority"))
                {
                    LogHandler.Err(new Exception("Each pattern in patterns db SHOULD have a 'priority' field!"));
                    continue;
                }

                jsonPattern.put("name", name);
                ThreatPattern threatPattern = createPattern(jsonPattern);
                listToFill.add(threatPattern);
            }
        }
        catch (JSONException e)
        {
            LogHandler.Err(e);
        }
    }
}