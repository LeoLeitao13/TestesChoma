# testes_selenium/test_orangehrm_selenium.py
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service as ChromeService
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_login_sucesso_orangehrm():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)

    driver.get("https://opensource-demo.orangehrmlive.com/")

    driver.find_element(By.NAME, "username").send_keys("Admin") # 
    driver.find_element(By.NAME, "password").send_keys("admin123") # [cite: 39]
    driver.find_element(By.TAG_NAME, "button").click()

    dashboard_header = driver.find_element(By.CSS_SELECTOR, ".oxd-topbar-header-breadcrumb h6")
    assert dashboard_header.text == "Dashboard"
    
    driver.find_element(By.CLASS_NAME, "oxd-userdropdown-tab").click()
    
    logout_link = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "a[href*='/logout']"))
    )
    logout_link.click()

    assert driver.find_element(By.NAME, "username").is_displayed()
    
    driver.quit()

def test_login_erro_orangehrm():
    driver = webdriver.Chrome(service=ChromeService(ChromeDriverManager().install()))
    driver.implicitly_wait(5)

    driver.get("https://opensource-demo.orangehrmlive.com/")

    driver.find_element(By.NAME, "username").send_keys("Admin")
    driver.find_element(By.NAME, "password").send_keys("wrong_password")
    driver.find_element(By.TAG_NAME, "button").click()

    error_message = driver.find_element(By.CLASS_NAME, "oxd-alert-content-text")
    assert error_message.is_displayed()
    assert "Invalid credentials" in error_message.text
    
    driver.quit()