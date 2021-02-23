package com.semicolon.spring.service.erp;

import com.semicolon.spring.dto.ErpDTO;
import com.semicolon.spring.exception.BadSupplyLinkException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErpServiceImpl implements ErpService{
    @Override
    public ErpDTO.Supply supply(ErpDTO.Url inputUrl) throws IOException {
        if(inputUrl.getUrl().contains("emart.ssg.com")){
            return emart(inputUrl.getUrl());
        }else if(inputUrl.getUrl().contains("www.yes24.com")) {
            return yes24(inputUrl.getUrl());
        }else if(inputUrl.getUrl().contains("www.popcone.co.kr")){
            return popcone(inputUrl.getUrl());
        }else if(inputUrl.getUrl().contains("www.s2b.kr")){
            return s2b(inputUrl.getUrl());
        }
        else throw new BadSupplyLinkException();
    }

    private ErpDTO.Supply emart(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();

        Element name = doc.selectFirst("h2.cdtl_info_tit");

        Elements option = doc.select("div.cdtl_item");
        List<String> options = new ArrayList<>();
        for(Element e : option.select("option")){
            options.add(e.text());
        }

        Elements image = doc.select("ul.lst_thmb");
        List<String> images = new ArrayList<>();
        for(Element e : image.select("img")){
            images.add(e.absUrl("src"));
        }


        return new ErpDTO.Supply(name.text(), options, images);
    }

    private ErpDTO.Supply yes24(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();

        Element name = doc.selectFirst("title");

        Elements image = doc.select("em.imgBdr");

        List<String> images = new ArrayList<>();

        for(Element e :image.select("img")){
            images.add(e.text());
        }

        return new ErpDTO.Supply(name.text(), new ArrayList<>(), images);
    }

    private ErpDTO.Supply popcone(String url) throws IOException{
        Document doc = Jsoup.connect(url).get();

        Element name = doc.selectFirst("div.desc_ti h2");

        Element image = doc.selectFirst("div.obj_pic");

        List<String> images = new ArrayList<>();
        for(Element e : image.select("img")){
            images.add(e.absUrl("src"));
        }

        return new ErpDTO.Supply(name.text(), new ArrayList<>(), images);

    }

    private ErpDTO.Supply s2b(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Element name = doc.selectFirst("font.f12_b_black");

        Element image = doc.selectFirst("td.detail_img");

        List<String> images = new ArrayList<>();
        for(Element e : image.select("img")){
            images.add(e.absUrl("src"));
        }

        return new ErpDTO.Supply(name.text(), new ArrayList<>(), images);
    }

}
