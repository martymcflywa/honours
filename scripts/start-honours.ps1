<#
  .SYNOPSIS
  @author Martin Ponce 10371381
  This script conveniently wraps building of the project, and execution of
  distributed services into a single command. The script assumes you have
  invoked it from the root folder of this repository, ie:
  ./scripts/start-honours.ps1

  This script assumes that java and maven executables exist in your path.
#>
$ErrorActionPreference = "Stop"

function Assert-PathExists()
{
  Param(
    [Parameter(Mandatory)]
    [string] $path
  )

  if (!(Test-Path -Path $path -PathType Any)) {
    throw "$path does not exist, try 'mvn clean verify' first."
  }
}

$Semver = "2.0.0"
$Interfaces = "./honours-interfaces/target/honours-interfaces-$Semver-SNAPSHOT.jar"
$PersistAssembly = "./honours-persist/target/honours-persist-$Semver-SNAPSHOT-jar-with-dependencies.jar"
$ServerAssembly = "./honours-server/target/honours-server-$Semver-SNAPSHOT-jar-with-dependencies.jar"
$ClientAssembly = "./honours-client/target/honours-client-$Semver-SNAPSHOT-jar-with-dependencies.jar"
$SecurityParam = "-Djava.security.policy=security.policy"
$HostnameParam = "-Djava.rmi.server.hostname=localhost"

$Wait = 30

Write-Output "Check java exists"
java -version
Write-Output "Check maven exists"
./mvnw.cmd -version
Write-Output "Building"
./mvnw.cmd clean package "-Dmaven.test.skip=true"
Write-Output "Check remote interfaces exists $Interfaces"
Assert-PathExists $Interfaces
Write-Output "Check persist executable exists $PersistAssembly"
Assert-PathExists $PersistAssembly
Write-Output "Check server executable exists $ServerAssembly"
Assert-PathExists $ServerAssembly
Write-Output "Check client executable exists $ClientAssembly"
Assert-PathExists $ClientAssembly

Write-Output "Add remote interfaces to classpath"
$env:CLASSPATH = $Interfaces
Write-Output $env:CLASSPATH

Write-Output "Start rmiregistry as background process"
$RmiProcess = Start-Process rmiregistry -PassThru
Write-Output "Wait $Wait seconds for rmiregistry to begin"
Start-Sleep -Seconds $Wait

Write-Output "Start honours persist as background process"
$PersistProcess = Start-Process java -ArgumentList "-jar", $SecurityParam, $HostnameParam, $PersistAssembly -PassThru
Write-Output "Wait $Wait seconds for honours persist to begin"
Start-Sleep -Seconds $Wait
Write-Output "Start honours server as background process"
$ServerProcess = Start-Process java -ArgumentList "-jar", $SecurityParam, $HostnameParam, $ServerAssembly -PassThru
Start-Sleep $Wait
Write-Output "Start honours client in foreground"
java -jar $SecurityParam $HostnameParam $ClientAssembly

Write-Output "Kill honours server"
Get-Process -InputObject $ServerProcess | Stop-Process
Write-Output "Kill honours persist"
Get-Process -InputObject $PersistProcess | Stop-Process
Write-Output "Kill rmiregistry"
Get-Process -InputObject $RmiProcess | Stop-Process
Write-Output "Done"