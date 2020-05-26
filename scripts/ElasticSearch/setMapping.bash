curl -X PUT "localhost:9200/tweetdata3?pretty" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "tweetId":    { "type": "integer" },
      "text":  { "type": "text"  },
      "url":   { "type": "keyword" , "index" : false },
      "highlight":   { "type": "text"  },
      "score":   { "type": "double"  },
      "discoverDate" : {"type" : "date" }
    }
  }
}
'

