@echo off
for /f "tokens=1 delims=:" %%A in ('git stash list') do (
    git stash apply %%A
)