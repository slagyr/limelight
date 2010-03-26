Statemachine.build do
  superstate :operational do
    on_entry :operate
    on_exit :beep
    state :locked do
      on_entry :lock
      event :coin, :unlocked
      event :pass, :locked, :alarm
    end
    state :unlocked do
      on_entry :unlock
      event :coin, :unlocked, :thanks
      event :pass, :locked
    end
    event :diagnose, :diagnostics
  end
  state :diagnostics do
    on_entry :disable
    on_exit :beep
    event :operate, :operational
  end
  stub_context :verbose => false
end