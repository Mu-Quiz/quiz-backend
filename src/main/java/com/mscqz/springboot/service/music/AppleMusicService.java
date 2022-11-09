package com.mscqz.springboot.service.music;

import com.mscqz.springboot.domain.music.Music;
import com.mscqz.springboot.domain.music.MusicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AppleMusicService {
    private final MusicRepository musicRepository;

    // jsonData를 JSONParser를 통해 분해
    public void init(String jsonData){
        try {
            JSONObject jObj;
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonData);
            JSONObject parseResponse = (JSONObject) jsonObject.get("results");
            JSONArray parseSongs = (JSONArray) parseResponse.get("songs");
            JSONObject parseArray = (JSONObject) parseSongs.get(0);
            String nextUrl = parseArray.get("next").toString();
            JSONArray musicInfoArray = (JSONArray) parseArray.get("data");

            for(int i=0;i<musicInfoArray.size();i++){
                jObj = (JSONObject) musicInfoArray.get(i);
                String appleMusicId = jObj.get("id").toString();
                JSONObject parseAttributes = (JSONObject) jObj.get("attributes");
                JSONObject parseArtwork = (JSONObject) parseAttributes.get("artwork");
                JSONArray parsePreviewsArray = (JSONArray) parseAttributes.get("previews");
                JSONObject parsePreviews = (JSONObject) parsePreviewsArray.get(0);
                String artworkUrl = parseArtwork.get("url").toString();
                String previewsUrl = parsePreviews.get("url").toString();
                String title = parseAttributes.get("name").toString();
                String artistName = parseAttributes.get("artistName").toString();

                Music music = Music.builder()
                        .appleMusicId(appleMusicId)
                        .title(title)
                        .artistName(artistName)
                        .artwork(artworkUrl)
                        .previews(previewsUrl)
                        .build();

                musicRepository.save(music);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
