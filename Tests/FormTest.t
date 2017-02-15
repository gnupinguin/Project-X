#!/usr/bin/env perl
use 5.014;
use utf8;
use open qw/:encoding(UTF-8) :std/;

use HTTP::Request::Common qw(POST); 
use LWP::Simple;

my $connectionString = "http://127.0.0.1:3000/file.html";

if ($ARGV[0] eq "host"){
	(undef, $connectionString) = (shift, shift);
}

my $connect = get($connectionString);

$connect =~ s/\n//g;

if (($connect =~ m(<div class="form-group">))&&($connect =~ m(<div class="form-group">))&&
($connect =~ m(<textarea required="required" name="quote" class="form-control))&&($connect =~m(</textarea>))&&($connect =~ m(<input required="required" name="author" type="text" class="form-control")) && ($connect =~ m(<input required="required" name="author" type="text" class="form-control"))&&($connect =~ m(<button type="submit"))) {say "looks like ok";}
else {say "looks like something missed";}

