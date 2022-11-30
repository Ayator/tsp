$files = Get-ChildItem bestOutput2/tsp_*
ForEach ($file in $files){
    $filename = $file.Name
    ./verifier input/$filename bestOutput2/$filename
}