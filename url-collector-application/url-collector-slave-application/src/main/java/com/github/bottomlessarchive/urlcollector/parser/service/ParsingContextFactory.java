package com.github.bottomlessarchive.urlcollector.parser.service;

import com.github.bottomlessarchive.urlcollector.parser.service.domain.ParsingContext;
import com.github.bottomlessarchive.warc.service.content.domain.WarcContentBlock;
import com.github.bottomlessarchive.warc.service.content.response.domain.ResponseContentBlock;
import com.github.bottomlessarchive.warc.service.record.domain.WarcRecord;
import org.springframework.stereotype.Service;

@Service
public class ParsingContextFactory {

    public ParsingContext buildParsingContext(final WarcRecord<WarcContentBlock> warcRecord) {
        final String warcRecordUrl = warcRecord.getHeader("WARC-Target-URI");
        final String contentString = ((ResponseContentBlock) warcRecord.getContentBlock()).getPayloadAsString();

        return ParsingContext.builder()
                .baseUrl(warcRecordUrl)
                .content(contentString)
                .build();
    }
}
