from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager

def test_login_sucesso_herokuapp():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)

    driver.get("https://the-internet.herokuapp.com/login") # [cite: 23]

    driver.find_element(By.ID, "username").send_keys("tomsmith") # [cite: 26]
    driver.find_element(By.ID, "password").send_keys("SuperSecretPassword!") # [cite: 28]
    driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()

    success_message = driver.find_element(By.ID, "flash")
    assert success_message.is_displayed()
    assert "You logged into a secure area!" in success_message.text

    driver.find_element(By.CSS_SELECTOR, "a[href='/logout']").click()
    assert driver.find_element(By.ID, "username").is_displayed()

    driver.quit()

def test_login_erro_herokuapp():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)
    
    driver.get("https://the-internet.herokuapp.com/login") # [cite: 23]

    driver.find_element(By.ID, "username").send_keys("usuario_errado")
    driver.find_element(By.ID, "password").send_keys("senha_errada")
    driver.find_element(By.CSS_SELECTOR, "button[type='submit']").click()
    
    error_message = driver.find_element(By.ID, "flash")
    assert error_message.is_displayed()
    assert "Your username is invalid!" in error_message.text # [cite: 29]

    driver.quit()