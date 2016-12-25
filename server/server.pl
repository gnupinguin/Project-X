#!/usr/bin/env perl
use Mojolicious::Lite;
use MongoDB;
use utf8;
use open qw/:encoding(UTF-8) :std/;


get '/' => sub {
  my $c = shift;
  $c->reply->static("practise.html");
} => "index";

post '/add' => sub {
  my $c = shift;
  $@ = undef;
  eval{
    my $client = MongoDB->connect(); # localhost, port 27107

    my $collection = $client->ns( 'QuotesDB.quotes' );
    $collection->insert_one({author => $c->param("author"), quotestext => $c->param("quote")});
  };
  if ($@){
    say "ERROR adding quotes!\n$@";
    $c->render(template => "status", message =>  "ERROR adding quotes!");
  }


  say "\n\nOK\n";
  $c->render(template => "status", message => "Quote added successfully!");
};

get '/data' => sub {
  my $c = shift;
  my $client = MongoDB->connect(); # localhost, port 27107

  my $collection = $client->ns( 'QuotesDB.quotes' );
  my $res = $collection->find();

  my $quotes;
  while (my $doc = $res->next) {
      push @$quotes, [$doc->{quotestext}, $doc->{author}];
      say "Quote: ".$doc->{quotestext};
  }
    $c->render(template => "data", quotes => $quotes);
};

app->start;
