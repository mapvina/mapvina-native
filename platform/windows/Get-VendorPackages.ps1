param(
	[Parameter(Mandatory=$true)][string]$Triplet,
	[Parameter(Mandatory=$true)][string]$Renderer,
    [Parameter(Mandatory=$false)][switch]${With-ICU}
)

$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $MyInvocation.MyCommand.Path -Parent)

$vcpkg_temp_dir = '***'

foreach($letter in [byte][char]'Z'..[byte][char]'A')
{
    $vcpkg_temp_dir = '{0}:' -f [char]$letter

    if(-not (Test-Path $vcpkg_temp_dir))
    {
        & subst $vcpkg_temp_dir ([System.IO.Path]::Combine($PWD.Path, 'vendor', 'vcpkg'))
        $env:VCPKG_ROOT = ('{0}\' -f $vcpkg_temp_dir)
        break
    }
}

switch($Renderer)
{
    'EGL'    { $renderer_packages = @('egl', 'opengl-registry'); break }
    'OpenGL' { $renderer_packages = @('opengl-registry');        break }
	'Vulkan' { $renderer_packages = @();                         break }
	'All'    { $renderer_packages = @('egl', 'opengl-registry'); break }
}

if(-not (Test-Path ('{0}\vcpkg.exe' -f $vcpkg_temp_dir)))
{
    $metadata = ConvertFrom-StringData (Get-Content ('{0}\scripts\vcpkg-tool-metadata.txt' -f $vcpkg_temp_dir) -Raw)
    $tool_name = if ($env:PROCESSOR_ARCHITECTURE -eq 'ARM64') { 'vcpkg-arm64.exe' } else { 'vcpkg.exe' }
    $download_url = 'https://github.com/microsoft/vcpkg-tool/releases/download/{0}/{1}' -f $metadata.VCPKG_TOOL_RELEASE_TAG, $tool_name
    try {
        Invoke-WebRequest -UseBasicParsing -Uri $download_url -OutFile ('{0}\vcpkg.exe' -f $vcpkg_temp_dir)
    } catch {
        subst $vcpkg_temp_dir /D
        throw
    }
}

try {
    & ('{0}\vcpkg.exe' -f $vcpkg_temp_dir) $(
    @(
        '--disable-metrics',
        ('--overlay-triplets={0}' -f [System.IO.Path]::Combine($PWD.Path, 'vendor', 'vcpkg-custom-triplets')),
        ('--triplet={0}' -f $Triplet),
        '--clean-after-build',
        'install', 'curl', 'dlfcn-win32', 'glfw3', 'libuv', 'libjpeg-turbo', 'libpng', 'libwebp'
    ) + $renderer_packages + $(if(${With-ICU}) {@('icu')} else {@()})
    )
    if ($LASTEXITCODE -ne 0) {
        throw "vcpkg install failed with exit code $LASTEXITCODE"
    }
} finally {
    subst $vcpkg_temp_dir /D
}
