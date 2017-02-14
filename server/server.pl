#!/usr/bin/env perl
use Mojolicious::Lite;
use MongoDB;
use utf8;
use open qw/:encoding(UTF-8) :std/;

my $dbConnectionString;

if ($ARGV[0] eq "dbconnect"){
	(undef, $dbConnectionString) = (shift, shift);
}

my $dbclient = MongoDB->connect($dbConnectionString); # if $dbConnectionString == undef - that default connection

get '/' => sub {
  my $c = shift;
  $c->reply->static("practise.html");
} => "index";

post '/add' => sub {
  my $c = shift;
  $@ = undef;
  eval{
    my $collection = $dbclient->ns( 'QuotesDB.quotes' );
		die "Empty author" if $c->param("author") =~ m/^\s*$/;
		die "Empty quote" if $c->param("quote") =~ m/^\s*$/;
    $collection->insert_one({author => $c->param("author"), quotestext => $c->param("quote")});
  };
  if ($@){
    say "ERROR adding quotes!\n$@";
    $c->render(template => "status", message =>  "ERROR adding quotes!\n$@", status => 401);
  }else{
		$c->render(template => "status", message => "Quote added successfully!");
	}

  #say "\n\nOK\n";

};

get '/data' => sub {
  my $c = shift;
  #my $client = MongoDB->connect($dbhost); # localhost, port 27107

  my $collection = $dbclient->ns( 'QuotesDB.quotes' );
  my $res = $collection->find();

  my $quotes;
  while (my $doc = $res->next) {
      push @$quotes, [$doc->{quotestext}, $doc->{author}];
      #say "Quote: ".$doc->{quotestext};
  }
    $c->render(template => "data", quotes => $quotes);
};

app->start;
